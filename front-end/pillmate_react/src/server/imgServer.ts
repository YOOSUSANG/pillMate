import express from 'express';
import path from 'path';
import multer from 'multer';
import cors from 'cors';

const app = express();
const port = 3001;

// Middleware to enable CORS
app.use(cors());

// 정적 파일 제공을 위한 미들웨어 등록
app.use('/userImg', express.static(path.join(__dirname, 'public/assets/userImg')));

// 이미지를 저장할 경로 설정
const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'public/assets/userImg');
    },
    filename: function (req, file, cb) {
        cb(null, file.originalname);
    },
});

const upload = multer({ storage: storage });

// 이미지 업로드 엔드포인트
app.post('/upload', upload.single('image'), (req, res) => {
    res.send('이미지 업로드 완료');
});

app.listen(port, () => {
    console.log(`서버가 포트 ${port}에서 실행 중입니다.`);
});
