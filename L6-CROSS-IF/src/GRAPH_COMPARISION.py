import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from pylab import *
import numpy as np
from matplotlib.font_manager import FontProperties

"""======================================================
@ Uses:  This file creates a graph showing differences

========================================================"""

#===========================================#
# Within project and Cross-project #
#===========================================#

#"""
path = "F:\\Research\\L6-CROSS-IF\\result\\"

"""
path = "E:\\Sangeeta\\Research\\L6-CROSS-IF\\result\\"
"""

#========================RQ4 =============================================#
#                       NB  Appraoch                    #
#==========================================================================#
#            Improvements in   LF                                          #
#==========================================================================#
plt.close()

avg_impr_LF_logoptplus_using_clif_nb=(8.15, 7.16, 6.29, 2.8, 8.18, 4.34, -2.67, -6.76)

ind = np.arange(8)
width = 0.3

fig, ax = plt.subplots()


# add some text for labels, title and axes ticks
plt.rcParams.update({'font.size': 15})
#ylim(0,20)
ax.set_ylabel('Average Improvement in LF (%)')
ax.set_xlabel("$CLIF_{NB}$ Classifier")
ax.set_title('Improvement in LF (%)')
ax.set_xticks(ind+width)
ax.set_xticklabels(('ADA', 'ADT', 'BN','J48', 'LOG',  'NB','RF', 'SVM'), rotation = 300)

#==== Command to change font size of legend ===#
fontP = FontProperties()
fontP.set_size('small')
rects1 = ax.bar(ind, avg_impr_LF_logoptplus_using_clif_nb, width,color ='#bebebe')
ax.plot([0., 7], [0, 0], "k-")

plt.tight_layout()
#plt.show()

plt.savefig(path+"clif-nb-avg-impr-lf.pdf")



#                       NB  Appraoch                                       #
#==========================================================================#
#            Improvements in   RA                                          #
#==========================================================================#
plt.close()

avg_impr_ra_logoptplus_using_clif_nb=(4.89, 4.11, -1.65, -6.65, 4.15,-0.37, -1.43, -6.24)

ind = np.arange(8)
width = 0.3

fig, ax = plt.subplots()

# add some text for labels, title and axes ticks
plt.rcParams.update({'font.size': 15})
#ylim(0,20)
ylim(-15,15)
ax.set_ylabel('Improvement in RA (%)')
ax.set_xlabel("$CLIF_{NB}$ Classifier")
ax.set_title('Average Improvement in RA (%)')
ax.set_xticks(ind+width)
ax.set_xticklabels(('ADA', 'ADT', 'BN','J48', 'LOG',  'NB','RF', 'SVM'), rotation = 300)

#==== Command to change font size of legend ===#
fontP = FontProperties()
fontP.set_size('small')
rects1 = ax.bar(ind, avg_impr_ra_logoptplus_using_clif_nb, width,color ='#bebebe')
ax.plot([0., 7], [0, 0], "k-")

plt.tight_layout()
#plt.show()

plt.savefig(path+"clif-nb-avg-impr-ra.pdf")



#========================RQ4 =============================================#
#                       BN  Appraoch                    #
#==========================================================================#
#            Improvements in   LF                                          #
#==========================================================================#
plt.close()

avg_impr_LF_logoptplus_using_clif_bn=(7.66, 8.21,3.01, 8.16, 2.08, -2.06, -8.92)

#@NOTE: BN is removed because it was not working on sone dataset  because of low probablity

ind = np.arange(7)
width = 0.3

fig, ax = plt.subplots()

# add some text for labels, title and axes ticks
plt.rcParams.update({'font.size': 15})
#ylim(0,20)
ax.set_ylabel('Improvement in LF (%)')
ax.set_xlabel("$CLIF_{BN}$ Classifier")
ax.set_title('Average Improvement in LF (%)')
ax.set_xticks(ind+width)
ax.set_xticklabels(('ADA', 'ADT', 'J48', 'LOG',  'NB','RF', 'SVM'), rotation = 300)

#==== Command to change font size of legend ===#
fontP = FontProperties()
fontP.set_size('small')
rects1 = ax.bar(ind, avg_impr_LF_logoptplus_using_clif_bn, width,color ='#bebebe')
ax.plot([0., 6], [0, 0], "k-")

plt.tight_layout()
#plt.show()

plt.savefig(path+"clif-bn-avg-impr-lf.pdf")


#                       BN  Appraoch                                       #
#==========================================================================#
#            Improvements in   RA                                          #
#==========================================================================#
plt.close()

avg_impr_ra_logoptplus_using_clif_bn=(-3.18, -1.96,  -12.18, -2.6,-7.44, -7.85, -8.54)
#@NOTE: BN is removed because it was not working on sone dataset  because of low probablity

ind = np.arange(7)
width = 0.3

fig, ax = plt.subplots()

# add some text for labels, title and axes ticks
plt.rcParams.update({'font.size': 15})
ylim(-15,15)
ax.set_ylabel('Improvement in RA (%)')
ax.set_xlabel("$CLIF_{BN}$ Classifier")
ax.set_title('Average Improvement in RA (%)')
ax.set_xticks(ind+width)
ax.set_xticklabels(('ADA', 'ADT', 'J48', 'LOG',  'NB','RF', 'SVM'), rotation = 300)

#==== Command to change font size of legend ===#
fontP = FontProperties()
fontP.set_size('small')
rects1 = ax.bar(ind, avg_impr_ra_logoptplus_using_clif_bn, width,color ='#bebebe')
ax.plot([0., 6], [0, 0], "k-")

plt.tight_layout()
#plt.show()

plt.savefig(path+"clif-bn-avg-impr-ra.pdf")