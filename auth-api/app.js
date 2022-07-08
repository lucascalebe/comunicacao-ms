import express from "express";
import * as db from "./src/config/db/initialData.js";
import userRoutes from "./src/modules/user/routes/UserRoutes.js";
import tracing from './src/config/tracing.js';

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;
const CONTAINER_ENV = "container";

app.get('/api/status', (req, res) => {
    return res.json({
        service: 'AUTH-API',
        status: 'UP',
        httpStatus: 200
    })
})

app.use(express.json());

startApplication();

function startApplication() {
    if (env.NODE_ENV !== CONTAINER_ENV) {
        db.createInitalData();
    }
}

app.get("/api/initial-data", (req,res) => {
    db.createInitalData();
    return res.json({message: "Data created." });
})

app.use(tracing);

app.use(userRoutes);

app.listen(PORT, () => {
    console.info(`Server started successfully at port ${PORT}`)
})