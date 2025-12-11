def input = getClass().getResource('/day7.txt').readLines()*.chars

int width = input.first.length
input = input.flatten()

int start = input.indexOf('S' as char)
def tracker = new long[width]
tracker[start] = 1

int splits = input.withIndex().inject(0) { tot, t ->
	def (chr, i) = t
	int x = i % width

	if (chr == '^') {
		def paths = tracker[x]
		if (paths > 0) {
			tracker[x - 1] += paths
			tracker[x + 1] += paths
			tracker[x] = 0
			tot++
		}
	}

	return tot
}

println "Splits: $splits"
println "Paths: ${tracker.sum()}"