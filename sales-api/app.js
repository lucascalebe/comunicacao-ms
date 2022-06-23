import express from "express";

import { connectMongoDb } from "./src/config/db/MongoDbConfig.js";
import { connectRabbitMq } from "./src/config/rabbitmq/rabbitConfig.js"
import { createInitialData } from "./src/config/db/initialData.js"; 
import checkToken from "./src/config/auth/checkToken.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

connectMongoDb();
createInitialData();
connectRabbitMq();

app.use(checkToken)

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