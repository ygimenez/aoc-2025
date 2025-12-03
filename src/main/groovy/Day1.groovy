def input = getClass().getResource('/day1.txt').readLines()

int zeros, dial = 50
(input =~ /([LR])(\d+)/).each { _, dir, num ->
	int val = num as int
	if (val > 99) {
		zeros += val / 100 as int
		val %= 100
	}

	int rot = dial + val * (dir == 'R' ? 1 : -1)
	if (rot < 0) {
		rot += 100
		if (dial != 0) zeros++
	}

	dial = (rot + 100) % 100
	zeros += dial ? (rot.abs() / 100 as int) : 1
}

println zeros