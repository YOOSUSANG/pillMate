import torchvision
import torchvision.transforms as transforms
import torch
import torch.nn as nn
import torch.optim as optim
import os
import torch.backends.cudnn as cudnn
from torchvision import models
from PIL import Image
import torch.nn.functional as F
import logging
train_dir =  'train 폴더 경로 지정'
test_dir = 'test 폴더 경로 지정'
realTest_dir = '이미지 예측을 원하는 폴더 경로 지정'
image_paths = [os.path.join(realTest_dir, filename) for filename in os.listdir(realTest_dir)]
val_percent = 0.2

class BasicBlock(nn.Module):
    expansion = 1

    def __init__(self, in_planes, planes, stride=1):
        super(BasicBlock, self).__init__()
        self.conv1 = nn.Conv2d(
            in_planes, planes, kernel_size=3, stride=stride, padding=1, bias=False)
        self.bn1 = nn.BatchNorm2d(planes)
        self.conv2 = nn.Conv2d(planes, planes, kernel_size=3,
                               stride=1, padding=1, bias=False)
        self.bn2 = nn.BatchNorm2d(planes)

        self.shortcut = nn.Sequential()
        if stride != 1 or in_planes != self.expansion*planes:
            self.shortcut = nn.Sequential(
                nn.Conv2d(in_planes, self.expansion*planes,
                          
                          kernel_size=1, stride=stride, bias=False),
                nn.BatchNorm2d(self.expansion*planes)
            )

    def forward(self, x):
        out = F.relu(self.bn1(self.conv1(x)))
        out = self.bn2(self.conv2(out))
        out += self.shortcut(x)
        out = F.relu(out)
        return out


class Bottleneck(nn.Module):
    expansion = 4

    def __init__(self, in_planes, planes, stride=1):
        super(Bottleneck, self).__init__()
        self.conv1 = nn.Conv2d(in_planes, planes, kernel_size=1, bias=False)
        self.bn1 = nn.BatchNorm2d(planes)
        self.conv2 = nn.Conv2d(planes, planes, kernel_size=3,
                               stride=stride, padding=1, bias=False)
        self.bn2 = nn.BatchNorm2d(planes)
        self.conv3 = nn.Conv2d(planes, self.expansion *
                               planes, kernel_size=1, bias=False)
        self.bn3 = nn.BatchNorm2d(self.expansion*planes)

        self.shortcut = nn.Sequential()
        if stride != 1 or in_planes != self.expansion*planes:
            self.shortcut = nn.Sequential(
                nn.Conv2d(in_planes, self.expansion*planes,
                          kernel_size=1, stride=stride, bias=False),
                nn.BatchNorm2d(self.expansion*planes)
            )

    def forward(self, x):
        out = F.relu(self.bn1(self.conv1(x)))
        out = F.relu(self.bn2(self.conv2(out)))
        out = self.bn3(self.conv3(out))
        out += self.shortcut(x)
        out = F.relu(out)
        return out


# class ResNet(nn.Module):
#     def __init__(self, block, num_blocks, num_classes=8):
#         super(ResNet, self).__init__()
#         self.in_planes = 64

#         self.conv1 = nn.Conv2d(3, 64, kernel_size=3,
#                                stride=1, padding=1, bias=False)
#         self.bn1 = nn.BatchNorm2d(64)
#         self.layer1 = self._make_layer(block, 64, num_blocks[0], stride=1)
#         self.layer2 = self._make_layer(block, 128, num_blocks[1], stride=2)
#         self.layer3 = self._make_layer(block, 256, num_blocks[2], stride=2)
#         self.layer4 = self._make_layer(block, 512, num_blocks[3], stride=2)
#         self.linear = nn.Linear(512*block.expansion, num_classes)

#     def _make_layer(self, block, planes, num_blocks, stride):
#         strides = [stride] + [1]*(num_blocks-1)
#         layers = []
#         for stride in strides:
#             layers.append(block(self.in_planes, planes, stride))
#             self.in_planes = planes * block.expansion
#         return nn.Sequential(*layers)

#     def forward(self, x):
#         out = F.relu(self.bn1(self.conv1(x)))
#         out = self.layer1(out)
#         out = self.layer2(out)
#         out = self.layer3(out)
#         out = self.layer4(out)
#         out = F.avg_pool2d(out, 4)
#         out = out.view(out.size(0), -1)  # 출력을 평탄화
#         out = self.linear(out)
#         return out
class ResNet(nn.Module):
    # 폴더 추가할떄마다 클래스 증가
    def __init__(self, block, num_blocks, num_classes=10):
        super(ResNet, self).__init__()
        self.in_planes = 64

        self.conv1 = nn.Conv2d(3, 64, kernel_size=7, stride=2, padding=3, bias=False)
        self.bn1 = nn.BatchNorm2d(64)
        self.layer1 = self._make_layer(block, 64, num_blocks[0], stride=1)
        self.layer2 = self._make_layer(block, 128, num_blocks[1], stride=2)
        self.layer3 = self._make_layer(block, 256, num_blocks[2], stride=2)
        self.layer4 = self._make_layer(block, 512, num_blocks[3], stride=2)
        self.avgpool = nn.AdaptiveAvgPool2d(1)  # 입력 크기에 상관없이 평균 풀링
        self.linear = nn.Linear(512*block.expansion, num_classes)
    
    def _make_layer(self, block, planes, num_blocks, stride):
        strides = [stride] + [1]*(num_blocks-1)
        layers = []
        for stride in strides:
            layers.append(block(self.in_planes, planes, stride))
            self.in_planes = planes * block.expansion
        return nn.Sequential(*layers)

    def forward(self, x):
        out = F.relu(self.bn1(self.conv1(x)))
        out = self.layer1(out)
        out = self.layer2(out)
        out = self.layer3(out)
        out = self.layer4(out)
        out = self.avgpool(out)
        out = out.view(out.size(0), -1)  # 출력을 평탄화
        out = self.linear(out)
        return out



def ResNet18():
    return ResNet(BasicBlock, [2, 2, 2, 2])


def ResNet34():
    return ResNet(BasicBlock, [3, 4, 6, 3])


def ResNet50():
    return ResNet(Bottleneck, [3, 4, 6, 3])


def ResNet101():
    return ResNet(Bottleneck, [3, 4, 23, 3])


def ResNet152():
    return ResNet(Bottleneck, [3, 8, 36, 3])


# def train(epoch,optimizer,criterion,net,device,train_loader,file_name):
#     print('\n[ Train epoch: %d ]' % epoch)
#     net.train()
#     train_loss = 0
#     correct = 0
#     total = 0
#     for batch_idx, (inputs, targets) in enumerate(train_loader):
#         inputs, targets = inputs.to(device), targets.to(device)
#         optimizer.zero_grad()
#         benign_outputs = net(inputs)
#         loss = criterion(benign_outputs, targets)
#         loss.backward()

#         optimizer.step()
#         train_loss += loss.item()
#         _, predicted = benign_outputs.max(1)

#         total += targets.size(0)
#         correct += predicted.eq(targets).sum().item()
        
#         if batch_idx % 100 == 0:
#             print('\nCurrent batch:', str(batch_idx))
#             print('Current benign train accuracy:', str(predicted.eq(targets).sum().item() / targets.size(0)))
#             print('Current benign train loss:', loss.item())

#     print('\nTotal benign train accuarcy:', 100. * correct / total)
#     print('Total benign train loss:', train_loss)
#     state = {
#         'net': net.state_dict()
#     }
#     if not os.path.isdir('checkpoint'):
#         os.mkdir('checkpoint')
#     torch.save(state, './checkpoint/' + file_name)
#     print('Model Saved!')
    
# def test(epoch,criterion,net,device,test_loader,classes):
#     print('\n[ Test epoch: %d ]' % epoch)
#     net.eval()
#     loss = 0
#     correct = 0
#     total = 0
    

#     for batch_idx, (inputs, targets) in enumerate(test_loader):
#         inputs, targets = inputs.to(device), targets.to(device)
#         total += targets.size(0)

#         outputs = net(inputs)
#         loss += criterion(outputs, targets).item()

#         _, predicted = outputs.max(1)
#         correct += predicted.eq(targets).sum().item()

#         predicted_class_names = [classes[p] for p in predicted]
#         true_class_names = [classes[t] for t in targets]

#         # print('Predicted Class Names:', predicted_class_names)
#         # print('True Class Names:', true_class_names)

#         # correct_labels = [classes[predicted[i]] == classes[targets[i]] for i in range(targets.size(0))]
#         # print('Correctly Classified:', correct_labels)

#     print('\nTest accuarcy:', 100. * correct / total)
#     print('Test average loss:', loss / total)


def predict(criterion, net, device, realtest_loader, classes):
    net.eval()
    loss = 0
    correct = 0
    total = 0
    idx = 1
    file_path = 'C:/spring/pillmate/src/main/pillmate-project/public/output.txt'
    # 파일 삭제
    try:
        os.remove(file_path)
        print(f'파일 삭제: {file_path}')
    except FileNotFoundError:
        print(f'삭제할 파일이 이미 존재하지 않습니다: {file_path}')
    except Exception as e:
        print(f'파일 삭제 중 오류 발생: {e}')


    for batch_idx, (inputs, targets) in enumerate(realtest_loader):
        inputs, targets = inputs.to(device), targets.to(device)
        total += targets.size(0)

        outputs = net(inputs)
        loss += criterion(outputs, targets).item()

        _, predicted = outputs.max(1)
        correct += predicted.eq(targets).sum().item()

        predicted_class_names = [classes[p] for p in predicted]

        for i in range(targets.size(0)):
           
    
            idx+=1
            with open('C:/spring/pillmate/src/main/pillmate-project/public/output.txt', 'w', encoding='utf-8') as log_file:
                log_file.write(predicted_class_names[i] + '\n')


    
def adjust_learning_rate(optimizer, epoch):
    lr = learning_rate
    if epoch >= 100:
        lr /= 10
    if epoch >= 150:
        lr /= 10
    for param_group in optimizer.param_groups:
        param_group['lr'] = lr
        
if __name__ == "__main__":
    transform_train = transforms.Compose([
    transforms.Resize(224),
    transforms.RandomCrop(224, padding=4),
    transforms.RandomHorizontalFlip(),
    transforms.ToTensor(),
    transforms.Normalize((0.4914, 0.4822, 0.4465), (0.2023, 0.1994, 0.2010))

    ])

    transform_test = transforms.Compose([
        transforms.ToTensor(),
        transforms.Normalize((0.4914, 0.4822, 0.4465), (0.2023, 0.1994, 0.2010))
    ])
    # 기존 CIFA10
    # train_dataset = torchvision.datasets.CIFAR10(root='./data', train=True, download=True, transform=transform_train)
    # test_dataset = torchvision.datasets.CIFAR10(root='./data', train=False, download=True, transform=transform_test)
    # train_dataset = torchvision.datasets.ImageFolder(root=train_dir, transform=transform_train)
    test_dataset = torchvision.datasets.ImageFolder(root=test_dir, transform=transform_test)
    realtest_dataset = torchvision.datasets.ImageFolder(root=realTest_dir, transform=transform_test)
    # print(train_dataset.classes)
    # print(test_dataset.classes)
    
    # 우리 datas{ TRAIN: 80, TEST: 20}

    # 3. Create data loaders
    # train_loader = torch.utils.data.DataLoader(train_dataset, batch_size=32, shuffle=True, num_workers=4)
    # test_loader = torch.utils.data.DataLoader(test_dataset, batch_size=32, shuffle=False, num_workers=4)
    realtest_loader = torch.utils.data.DataLoader(realtest_dataset, batch_size=1, shuffle=False, num_workers=4)
    

    # print(f'''Starting training:
    #         Training size:   {len(train_dataset)}
    #         Test size: {len(test_dataset)}
    #     ''')
    device = 'cuda'
    # Use ResNet-18 from torchvision
    # net = models.resnet18()
    net = ResNet34()
    net = net.to(device)
    net = torch.nn.DataParallel(net)
    file_name = 'resnet34_pill.pth'

    # 파일 경로 확인하여 체크포인트 존재 여부 판단
    checkpoint_path = '체크포인트 경로 지정' + file_name
    if os.path.isfile(checkpoint_path):
        # 체크포인트 파일이 존재하는 경우, 기존 체크포인트 불러오기
        state_dict = torch.load(checkpoint_path)
        net.load_state_dict(state_dict['net'])
        # print('Existing checkpoint found and loaded.')
    else:
        # 체크포인트 파일이 없는 경우, 모델을 처음부터 훈련
        print('No existing checkpoint found. Training the model from scratch.')

    
    # Count the total number of parameters in the model
    # total_params = sum(p.numel() for p in net.parameters())
    # print(f"Total number of parameters in ResNet-18: {total_params}")
    cudnn.benchmark = True

    learning_rate = 0.1

    criterion = nn.CrossEntropyLoss()
    optimizer = optim.SGD(net.parameters(), lr=learning_rate, momentum=0.9, weight_decay=0.0001)
    scheduler = torch.optim.lr_scheduler.CosineAnnealingLR(optimizer, T_max=10)
    
    predict(criterion,net,device,realtest_loader,test_dataset.classes)
    
    # for epoch in range(0, 100):
    #     adjust_learning_rate(optimizer, epoch)
    #     train(epoch,optimizer,criterion,net,device,train_loader,file_name)
    #     test(epoch,criterion,net,device,test_loader,test_dataset.classes)
    #     scheduler.step()
