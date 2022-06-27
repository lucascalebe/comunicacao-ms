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
            await this.validateProductStock(order);
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

    async updateOrder(orderMessage) {
        try {
            const order = JSON.parse(orderMessage);
            if (order.salesId && order.status) {
                let existingOrder = await OrderRepository.findById(order.salesId);
                if (existingOrder && order.status !== existingOrder.status) {
                    existingOrder.status = order.status;
                    await OrderRepository.save(existingOrder);
                }
            } else {
                console.warn("The order message was not complete.");
            }
        } catch (err) {
            console.error("Could not parse order message from queue.");
            console.error(err.message);
        }
    }

    validateOrderData(data) {
        if (!data || !data.products) {
            throw new OrderException(BAD_REQUEST,'The product must be informed.');
        }
    }

    async validateProductStock(order) {
        let stockIsOut = true;
        if (stockIsOut) {
            throw new OrderException(BAD_REQUEST, "The stock is out for the products");
        }
    }
}

export default new OrderService();