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
const CONTAINER_ENV="container";
const THREE_MINUTES=180000;

startApplication();

async function startApplication() {
    if (CONTAINER_ENV === env.NODE_ENV) {
        console.info("Waiting for RabbitMq and MongoDB to start...");
        setInterval(() => {
            connectMongoDb();
            connectRabbitMq();
        }, THREE_MINUTES);
    } else {
        connectMongoDb();
        createInitialData();
        connectRabbitMq();
    }
}

app.use(express.json());

app.get("/api/initial-data", (req,res) => {
    createInitialData();
    return res.json({message: "Data created." });
})

app.use(tracing);
app.use(checkToken);
app.use(orderRoutes);

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