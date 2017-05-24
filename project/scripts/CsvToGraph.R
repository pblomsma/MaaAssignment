#Get inputfile
args = commandArgs(trailingOnly=TRUE)

if (length(args)==0) {
  args[1] <-  "C:/Projects/Agents/MAA/output/03.09.0100000010.03.01000.01000.0100000010100.30000000000000004.csv"
  #stop("You should supply an inputfile", call.=FALSE)
}
  
MALData = read.csv(args[1], sep=";")

library(ggplot2)
currentplot <- ggplot(data = MALData, aes(x = round, y = mean, group = action, color = action)) + geom_smooth()

outputfile <- paste(trimws(args[1]), "_Rplot.jpg")

print(outputfile)

jpeg(filename = outputfile, width = 2000, height = 1500)
print(currentplot)