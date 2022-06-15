import express from "express";

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

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