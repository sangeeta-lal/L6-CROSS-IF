import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from pylab import *
import numpy as np


"""======================================================
@ Uses:  This file creates a graph showing diffeence in
within-project and cross-project logging prediction
========================================================"""

#===========================================#
# Within project and Cross-project #
#===========================================#

#"""
path = "F:\\Research\\L6-CROSS-IF-ISEC2017\\result\\"

"""
path = "E:\\Sangeeta\\Research\\L6-CROSS-IF-ISEC2017\\result\\"
"""


#========================RQ1 =============================================#
#  #
#==========================================================================#
#               LF                                                        #     
#=========================================================================#
plt.close()


AVGLF = (37.35, 55.47, 31.98, 7.45, 11.25, 3.14, 9.16, 2.31, 9.67)
ind = np.arange(9)
width = 0.4

fig, ax = plt.subplots()
barlist = ax.bar(ind, AVGLF, width,color ='r', linewidth=1)
# add some text for labels, title and axes ticks
plt.rcParams.update({'font.size': 13})
barlist[0].set_color('#bebebe')
barlist[1].set_color('#bebebe')
barlist[2].set_color('#bebebe')
barlist[3].set_color('#000000')
barlist[4].set_color('#000000')
barlist[5].set_color('#000000')
barlist[6].set_color('#000000')
barlist[7].set_color('#000000')
barlist[8].set_color('#000000')

#== Border ===#
barlist[0].set_edgecolor("#000000")
barlist[1].set_edgecolor("#000000")
barlist[2].set_edgecolor("#000000")
barlist[3].set_edgecolor("#000000")
barlist[4].set_edgecolor("#000000")
barlist[5].set_edgecolor("#000000")
barlist[6].set_edgecolor("#000000")
barlist[7].set_edgecolor("#000000")

ax.set_ylabel('Average LF (%)')
ax.set_xlabel('Source Project -> Target Project')
#ax.set_title('Within-project vs. Cross-project Logging Prediction')
ax.set_xticks(ind + width)
labels =  ['TC->TC', 'CS->CS', 'HD->HD', 'CS->TC', 'HD->TC', 'TC->CS', 'HD->CS', 'TC->HD', 'CS->HD']
plt.xticks(ind+width, labels, rotation =340)

green_patch = mpatches.Patch(color='#d3d3d3', label='Within-project')
red_patch = mpatches.Patch(color='#000000', label='Cross-project')
plt.legend(handles=[green_patch, red_patch])

ylim(0,110)
plt.tight_layout()
#plt.show()
plt.savefig(path+"within-cross.pdf")

