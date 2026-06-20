const express = require('express');
const http = require('http');
const {Server} = require('socket.io');
const cors = require('cors');
const Message = require('./MsgModel');
const connectDb = require('./connectDb');
const jwt = require('jsonwebtoken');
const dotenv = require('dotenv');
dotenv.config();

connectDb();


const PORT = process.env.PORT || 3000;

const app = express();
app.use(express.json());

const server = http.createServer(app);

const io = new Server(server, {
    cors: {
        origin: [process.env.FRONTEND_URL, "http://localhost:5174", "http://localhost:5175"],
        methods: ["GET", "POST"],
    }
});

app.use(cors({
    origin: [process.env.FRONTEND_URL, "http://localhost:5174", "http://localhost:5175"],
}));

const verifyToken = (req, res, next) => {
    const token = req.headers.authorization?.split(' ')[1];
    if (!token) return res.status(401).json({ error: 'No token provided' });
    try {
        req.user = jwt.verify(token, process.env.JWT_SECRET);
        next();
    } catch (err) {
        res.status(401).json({ error: 'Invalid token' });
    }
};

app.get('/messages/:userId1/:userId2', verifyToken, async (req, res) => {
    try {
        const { userId1, userId2 } = req.params;
        const messages = await Message.find({
            $or: [
                { senderId: userId1, receiverId: userId2 },
                { senderId: userId2, receiverId: userId1 },
            ],
        }).sort({ createdAt: 1 });
        res.json(messages);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// userId -> socketId mapping
const onlineUsers = new Map();

io.use((socket, next) => {
    const token = socket.handshake.auth.token;
    if (!token) {
        console.log('Socket auth failed: missing token');
        return next(new Error('Authentication error'));
    }

    jwt.verify(token, process.env.JWT_SECRET, (err, decoded) => {
        if (err) {
            console.log('JWT verification failed:', err.message);
            return next(new Error('Authentication error'));
        }
        const userId = decoded?.userId || decoded?.sub || decoded?.id;
        socket.data.userId = String(userId ?? '');
        next();
    });
});

io.on('connection', (socket) => {
    console.log('A user connected: ' + socket.id);

    socket.on("user_online", (userId) => {
        const normalizedUserId = String(userId);
        onlineUsers.set(normalizedUserId, socket.id);
        socket.data.userId = normalizedUserId;

        // Tell the newly connected client about all users already online.
        onlineUsers.forEach((socketId, onlineUserId) => {
            socket.emit("user_status", { userId: onlineUserId, status: "online" });
        });

        // Broadcast the new user's online status to everyone else.
        socket.broadcast.emit("user_status", { userId: normalizedUserId, status: "online" });
    });

    socket.on("join_room", (currentUserId, otherUserId) => {
        const roomId = [currentUserId, otherUserId].sort().join("_");
        socket.join(roomId);
        console.log(`User ${currentUserId} joined room: ${roomId}`);
    });

    socket.on("typing", ({ currentUserId, otherUserId, isTyping }) => {
        const roomId = [currentUserId, otherUserId].sort().join("_");
        socket.to(roomId).emit("typing_status", { userId: currentUserId, isTyping });
    });

    socket.on("send_message", async (data) => {
        const { currentUserId, otherUserId, message, fileUrl } = data;
        const roomId = [currentUserId, otherUserId].sort().join("_");
        const createdAt = new Date().toISOString();
        io.to(roomId).emit("receive_message", {
            senderId: currentUserId,
            receiverId: otherUserId,
            message,
            fileUrl,
            createdAt,
        });
        try {
            const newMessage = await Message.create({
                senderId: currentUserId,
                receiverId: otherUserId,
                message: message || '',
                fileUrl,
            });
            console.log(`Message from ${currentUserId} to ${otherUserId} in room ${roomId}: ${newMessage.message}`);
        } catch (err) {
            console.error('Failed to save message:', err.message);
        }
    });

    socket.on("start_video_call", ({ currentUserId, otherUserId }) => {
        const roomId = [currentUserId, otherUserId].sort().join("_");
        const payload = { callerId: currentUserId, roomId, targetUserId: String(otherUserId) };
        io.emit("incoming_video_call", payload);
        console.log(`Video call started by ${currentUserId} to ${otherUserId} in room ${roomId} with payload`, payload);
    });

    socket.on('disconnect', () => {
        const userId = socket.data.userId;
        if (userId) {
            onlineUsers.delete(userId);
            io.emit("user_status", { userId, status: "offline" });
        }
        console.log('A user disconnected: ' + socket.id);
    });
});


server.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});