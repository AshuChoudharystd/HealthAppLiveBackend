const mongoose = require('mongoose');

const msgSchema = new mongoose.Schema({
    senderId: { type: String, required: true },
    receiverId: { type: String, required: true },
    message: { type: String, default: '' },
    fileUrl: { type: String },
    createdAt:{ type: Date, default: Date.now },
});

module.exports = mongoose.model('Message', msgSchema);