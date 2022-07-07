import Order from "../../modules/sales/model/Order.js";
import {v4 as uuidv4} from "uuid";

export async function createInitialData() {
    if (Order.length > 0) {
        await Order.collection.drop();
    }
    await Order.create({
        products: [
            {
                productId: 1000,
                quantity: 2
            },
            {
                productId: 1001,
                quantity: 1
            },
            {
                productId: 1002,
                quantity: 1
            },
        ],
        user: {
            id: 'abcedfg123456',
            name: 'User Test',
            email: 'usertest@gmail.com'
        },
        status: 'APPROVED',
        createdAt: new Date(),
        updatedAt: new Date(),
        transactionid: uuidv4(),
        serviceid: uuidv4()
    });
    await Order.create({
        products: [
            {
                productId: 1000,
                quantity: 4
            },
            {
                productId: 1002,
                quantity: 2
            },
        ],
        user: {
            id: '12345abcdf',
            name: 'User Test 2',
            email: 'usertest2@gmail.com'
        },
        status: 'REJECTED',
        createdAt: new Date(),
        updatedAt: new Date(),
        transactionid: uuidv4(),
        serviceid: uuidv4()
    });
    let initialData = await Order.find();
    console.info(`Intial data was created: ${JSON.stringify(initialData, undefined, 4)}`)
}