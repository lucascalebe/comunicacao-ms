import express from "express";

import { connectMongoDb } from "./src/config/db/MongoDbConfig.js";
import { connectRabbitMq } from "./src/config/rabbitmq/rabbitConfig.js"
import { createInitialData } from "./src/config/db/initialData.js"; 
import checkToken from "./src/config/auth/checkToken.js";
import { sendMessageToProductStockUpdateQueue } from "./src/modules/product/rabbitmq/productStockUpdateSender.js";
import orderRoutes from "./src/modules/sales/routes/OrderRoutes.js";
import tracing from './src/config/tracing.js';

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

app.use(tracing);
connectMongoDb();
//createInitialData();
connectRabbitMq();

app.use(express.json());
app.use(checkToken);
app.use(orderRoutes);

app.get('/test', (req, res) => {
    try {
        sendMessageToProductStockUpdateQueue([
            {
                productId: 1001,
                quantity: 3
            },
            {
                productId: 1002,
                quantity: 2
            },
            {
                productId: 1003,
                quantity: 1
            },
        ])
        return res.status(200).json({ status: 200 })
    } catch (err) {
       console.error(err)
       return res.status(500).json({ error: true })
    }
})

app.get('/api/status', async (req, res) => {
    return res.json({
        service: 'sales-API',
        status: 'UP',
        httpStatus: 200
    })
})

app.listen(PORT, () => {
    console.info(`Server started successfully at port ${PORT}`)
})