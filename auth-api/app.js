import express from "express";
import * as db from "./src/config/db/initialData.js";
import userRoutes from "./src/modules/user/routes/UserRoutes.js";
import tracing from './src/config/tracing.js';

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

db.createInitalData();

app.use(tracing);
app.use(express.json());

app.use(userRoutes);

app.get('/api/status', (req, res) => {
    return res.json({
        service: 'AUTH-API',
        status: 'UP',
        httpStatus: 200
    })
})

app.listen(PORT, () => {
    console.info(`Server started successfully at port ${PORT}`)
})