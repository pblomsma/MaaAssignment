MALData = read.csv("C:\\Projects\\Agents\\tmp\\all.csv", sep=";")

#renaming for facets
index <- MALData$radius == 1
MALData$radius[index] <- "Radius 1"
index <- MALData$radius == 4
MALData$radius[index] <- "Radius 4"

index <- MALData$number.of.actions == 2
MALData$number.of.actions[index] <- "2 actions"

index <- MALData$number.of.actions == 3
MALData$number.of.actions[index] <- "3 actions"

index <- MALData$number.of.actions == 4
MALData$number.of.actions[index] <- "4 actions"

index <- MALData$number.of.actions == 5
MALData$number.of.actions[index] <- "5 actions"

library(ggplot2)
library("ggthemes")


ggplot(data = MALData, aes(x = round, y = mean, group = action, color = action)) + geom_smooth() + facet_grid(radius ~ number.of.actions) + labs(x = "Round", y = "Mean reward") + ggtitle("Mean rewards per action for different radii and action space sizes") + theme(plot.title = element_text(lineheight=.8, face="bold"))
