import amqp from "amqplib/callback_api.js";
import { RABBIT_MQ_URL } from "../constants/secrets.js";
import {
    PRODUCT_TOPIC,
    PRODUCT_STOCK_UPDATE_QUEUE,
    PRODUCT_STOCK_UPDATE_ROUTING_KEY,
    SALES_CONFIRMATION_QUEUE,
    SALES_CONFIRMATION_ROUTING_KEY
} from "./queue.js";

const HALF_SECOND = 500;
const HALF_MINUTE = 30000;
const CONTAINER_ENV = "container";

export async function connectRabbitMq() {
    const env = process.env.NODE_ENV;
    console.log(env)
    if (CONTAINER_ENV === env) {
        console.info("Waiting for RabbitMq to start...");
        setInterval(() => {
            connectRabbitMqAndCreateQueues();
        }, HALF_MINUTE);
    } else {
        connectRabbitMqAndCreateQueues();
    }
}

async function connectRabbitMqAndCreateQueues() {
    amqp.connect(RABBIT_MQ_URL, (error, connection) => {
        if (error) {
            throw error;
        }
        console.info("Starting RabbitMq...")
        createQueue(connection, PRODUCT_STOCK_UPDATE_QUEUE, PRODUCT_STOCK_UPDATE_ROUTING_KEY, PRODUCT_TOPIC);
        createQueue(connection, SALES_CONFIRMATION_QUEUE, SALES_CONFIRMATION_ROUTING_KEY, PRODUCT_TOPIC);
        console.log("Queue and topics were defined.")
        setTimeout(function () {
            connection.close();
        }, HALF_SECOND);
    });
}

function createQueue(connection, queue, routingKey, topic) {
    connection.createChannel((error, channel) => {
        if (error) {
            throw error;
        }
        channel.assertExchange(topic, 'topic', { durable: true })
        channel.assertQueue(queue, { durable: true })
        channel.bindQueue(queue, topic, routingKey);
    });
}