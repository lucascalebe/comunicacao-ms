import express from "express";
import checkToken from "./src/config/auth/checkToken.js";
import * as db from "./src/config/db/initialData.js";
import userRoutes from "./src/modules/user/routes/UserRoutes.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

db.createInitalData();

app.use(express.json());

app.use(userRoutes);

app.use(checkToken);

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