# app.py

from flask import Flask, request, jsonify, render_template
from flask_cors import CORS
import os
from PIL import Image
import subprocess
from flask_socketio import SocketIO, emit

app = Flask(__name__)
CORS(app)
socketio = SocketIO(app, cors_allowed_origins="*")

# 인식된 이미지가 들어갈 폴더 경로
UPLOAD_FOLDER = ''
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# Create "uploads" directory if it doesn't exist
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)

def resize_image(file_path, target_size=(224, 224)):
    image = Image.open(file_path)
    resized_image = image.resize(target_size)
    resized_image.save(file_path)

def clear_folder(folder_path):
    # Remove all files in the folder
    for file_name in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file_name)
        os.remove(file_path)

@app.route('/upload', methods=['POST'])
def upload_file():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'})

    file = request.files['file']

    if file.filename == '':
        return jsonify({'error': 'No selected file'})

    if file:
        # Clear the folder before saving a new image
        clear_folder(app.config['UPLOAD_FOLDER'])

        filename = os.path.join(app.config['UPLOAD_FOLDER'], file.filename)
        file.save(filename)

        # Resize the uploaded image to 224x224
        resize_image(filename)

        # Call the other Python script
        # pillResnet.py를 불러와주세요.
        script_path = ''

        # Use subprocess.run to capture the output
        subprocess.run(['python', script_path])

        # Read the output file
        # 약 이름이 저장된 경로를 열어주세요.
        with open('', 'r', encoding='utf-8') as file:
            output_result = file.read()

        print("Emitting prediction result:", output_result)
        # Emit the result to connected clients
        # socketio.emit('prediction_result', {'result': output_result})
        socketio.emit('prediction', {"result": 'Hello from server'})

        return jsonify({'message': 'File uploaded, resized, and script executed successfully', 'filename': filename})


if __name__ == '__main__':
    socketio.run(app, debug=True)
