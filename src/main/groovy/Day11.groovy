def input = getClass().getResource('/day11.txt').readLines()

def nodeMap = [:] as Map<String, List<String>>
for (e in input) {
	def (head, nodes) = e.split(': ').collect { it }
	nodes = nodes.split(' ').collect { it }

	nodeMap[head] = nodes
}

def memo = [:] as Map<String, Long>

long traverse(String node, String start = node, Map<String, List<String>> nodes, Map<String, Long> memo, int found = 0) {
	if (node == 'out') {
		if (start == 'svr') {
			return found == 0b11 ? 1 : 0
		}

		return 1
	}

	String key = "$node-$found"
	if (memo.containsKey(key)) {
		return memo[key]
	}

	int seen = found
	switch (node) {
		case 'fft' -> seen |= 0b1
		case 'dac' -> seen |= 0b10
	}

	long total = 0
	if (nodes.containsKey(node)) {
		for (n in nodes[node]) {
			total += traverse(n, start, nodes, memo, seen)
		}
	}

	return memo[key] = total
}

println "Paths (from me): ${traverse('you', nodeMap, memo)}"

memo.clear()

println "Paths (from server): ${traverse('svr', nodeMap, memo)}"