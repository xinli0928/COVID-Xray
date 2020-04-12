import torch
import torch.nn as nn
from torchvision import models

DEVICE = torch.device('cpu')   # 'cpu' in this case
num_classes = 3
squeezenet = models.squeezenet1_0(pretrained=True)
squeezenet.classifier[1] = nn.Conv2d(512, num_classes, kernel_size=(1,1), stride=(1,1))
squeezenet = torch.load('SqueezeNet_Noisy.pkl')
squeezenet.to(DEVICE);
model = squeezenet
input_tensor = torch.rand(1,3,224,224)
script_model = torch.jit.trace(model, input_tensor)
script_model.save("SqueezeNet_Noisy.pt")
