import torch
import torch.nn as nn
import torch.nn.functional as F

class MarginLoss(nn.Module):
    """Margin Loss
    Args:
        num_classes (int): number of classes.

    """
    def __init__(self, num_classes=10, margin=0.995, use_gpu=True):
        super(MarginLoss, self).__init__()
        self.margin = margin
        self.num_classes = num_classes
        self.use_gpu = use_gpu


    def forward(self, x, labels):
        """
        Args:
            x: feature matrix with shape (batch_size, feat_dim).
            labels: ground truth labels with shape (batch_size).
        """
        batch_size = x.size(0)
        classes = torch.arange(self.num_classes).long()
        if self.use_gpu: classes = classes.cuda()
        labels = labels.unsqueeze(1).expand(batch_size, self.num_classes)
        mask = labels.eq(classes.expand(batch_size, self.num_classes))
    	#prob = F.softmax(self.centers, dim=1)
        x = F.softmax(x,dim=1)
        p_gt = torch.mul(mask.float(), x).sum(dim=1, keepdim=True).expand(batch_size, self.num_classes)
        
        diff = self.margin + x - p_gt
        zeros = torch.zeros(batch_size, self.num_classes).cuda()
        loss = (torch.max(zeros, diff).sum() / batch_size - self.margin)  / (self.num_classes - 1)

    
        return loss
