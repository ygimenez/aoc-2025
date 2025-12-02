def input = getClass().getResource('/day1.txt').readLines()

int zeros, dial = 50
(input =~ /([LR])(\d+)/).each { _, dir, num ->
	dial = (dial + (num as int) * (dir == 'R' ? 1 : -1) + 100) % 100
	if (dial == 0) zeros++
}

println zeros