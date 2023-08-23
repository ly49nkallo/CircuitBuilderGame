import sys, os
from pathlib import Path
import matplotlib.pyplot as plt
import itertools
import numpy as np

if not (len(sys.argv) == 3 or len(sys.argv) == 2):
    print("USAGE python(3) remove_background.py <FILEPATH> <OUTPUT>")
    exit(1)

if (len(sys.argv) == 2):
    out_path = Path(sys.argv[1])
else:
    out_path = Path(sys.argv[2])

path = Path(sys.argv[1])
ext = sys.argv[1].split('.')[-1].lower()
supported_formats = ['png']

if len(sys.argv) == 3:
    if sys.argv[2].split('.')[-1].lower() != ext:
        print("extensions not the same")
        exit(1)

if ext not in supported_formats:
    print("filetype not supported")
    exit(1)

im = plt.imread(path)
out = im.copy()

rgb = (out.shape[-1] == 3)
rgba = (out.shape[-1] == 4)

print(im.shape)
print(im.dtype)
print(im.min())
print(im.max())
print('EXTENSION', ext)
dt = im.dtype

if ext == 'png':
    if rgb:
        out = np.concatenate((out, np.ones((*out.shape[:-1], 1))), axis=2)
    for i in range(im.shape[0]):
        for j in range(im.shape[1]):
            if np.all(im[i,j] == 1.):
                out[i,j] = np.array([0.,0.,0.,0.], dtype=dt)
                
if os.path.isfile(out_path):
    os.remove(out_path)
plt.imshow(out)
plt.show()
plt.imsave(out_path, out)
