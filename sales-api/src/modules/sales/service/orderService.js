import OrderRepository from '../repository/orderRepository.js';
import { sendMessageToProductStockUpdateQueue } from '../../product/rabbitmq/productStockUpdateSender.js'
import { PENDING, ACCEPTED, REJECTED } from '../status/orderStatus.js'
import OrderException from '../exception/orderException.js'
import { BAD_REQUEST } from '../../../config/constants/httpStatus.js'

class OrderService {
    async createOrder(req) {
        try {
            let orderData = req.body;
            this.validateOrderData(orderData);
            const { authUser } = req;
            let order = {
                status: PENDING,
                user: authUser,
                createdAt: new Date(),
                updatedAt: new Date(),
                products: orderData
            }
            let createdOrder = await OrderRepository.save(order);
            sendMessageToProductStockUpdateQueue(createdOrder.products)
            return {
                status: httpStatus.SUCCESS,
                createdOrder
            }
        } catch (err) {
            return {
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message
            }
        }
    }

    validateOrderData(data) {
        if (!data || !data.products) {
            throw new OrderException(BAD_REQUEST,'The product must be informed.');
        }
    }
}

export default new OrderService();