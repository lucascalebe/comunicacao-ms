import express from "express";

import { connect } from "./src/config/db/MongoDbConfig.js";
import Order from "./src/modules/sales/model/Order.js";


const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

connect();

app.get('/api/status', async (req, res) => {
    let test = await Order.find();
    console.log(test)
    return res.json({
        service: 'sales-API',
        status: 'UP',
        httpStatus: 200
    })
})

app.listen(PORT, () => {
    console.info(`Server started successfully at port ${PORT}`)
})