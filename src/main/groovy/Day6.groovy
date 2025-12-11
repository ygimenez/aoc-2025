boolean NORMAL = false
def input = getClass().getResource('/day6.txt').readLines()
def ops = [
		'*': { a, b -> a * b },
		'+': { a, b -> a + b }
]

int i = 0
if (NORMAL) {
	input = input*.trim()*.split(/\s+/) as List<List<String>>

	println input.removeLast().collect {
		return input
				*.getAt(i++)
				*.toLong()
				.inject(ops[it])
	}.sum()
} else {
	int lineSize = input.inject(0) { a, b -> Math.max(a, b.length()) }
	def buffer = []
	long word = 0

	//noinspection GroovyAssignabilityCheck
	println input*.padRight(lineSize)
			*.reverse()
			*.chars
			.transpose()
			.flatten()
			.inject([]) { tot, char v ->
				int val = v - 48
				if (val >= 0 && val <= 9) {
					word = word * 10 + val
				} else if (word > 0) {
					buffer << word
					word = 0
				}

				def op = ops[v as String]
				if (op) {
					tot << buffer.inject(op)
					buffer.clear()
				}

				return tot
			}.sum()
}