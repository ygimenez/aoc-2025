import org.ojalgo.optimisation.ExpressionsBasedModel
import java.util.regex.Pattern

def input = getClass().getResource('/day10.txt').readLines()

record Machine(int target, int[] buttons, int[] jolt, int[] solution) {
	static final Pattern PATTERN = Pattern.compile(/\[[.#]*]|\(\d+(?:,\d+)*\)|\{\d+(?:,\d+)*}/)

	static Machine parse(String line) {
		int target = 0
		def buttons = [] as List<Integer>
		int[] jolt = new int[0]

		line.findAll(PATTERN).each {
			def content = it[1..<-1]
			switch (it.charAt(0)) {
				case '[' -> {
					target = Integer.parseInt(content.reverse().replace(['.': '0', '#': '1']), 2)
				}
				case '(' -> {
					buttons << content.split(',')
							.collect { it as int }
							.inject(0) { mask, n -> mask | (1 << n) }
				}
				case '{' -> {
					jolt = content.split(',')
							.collect { it as int }
							.toArray() as int[]
				}
			}
		}

		return new Machine(target, buttons as int[], jolt, null)
	}

	Machine solve(int[] solution) {
		return new Machine(target, buttons, jolt, solution)
	}

	@Override
	String toString() {
		return "Machine[${hashCode()}]"
	}
}

static Machine bruteforce(Machine m) {
	def smallest = null as List<Integer>
	def seq = [] as List<Integer>

	int max = 2**m.buttons().length
	for (int i = 1; i < max; i++) {
		if (smallest != null && Integer.bitCount(i) >= smallest.size()) continue

		int val = 0
		for (int j = 0, buffer = i; buffer > 0; j++, buffer >>= 1) {
			if ((buffer & 1) == 0) continue

			def btn = m.buttons()[j]
			seq << btn

			if ((val ^= btn) == m.target()) {
				if (smallest == null || seq.size() < smallest.size()) {
					smallest = seq.toList()
				}
			}
		}

		seq.clear()
	}

	if (smallest) {
		return m.solve(smallest.toArray() as int[])
	}

	throw new RuntimeException("$m has no solution")
}

def machines = input.collect { bruteforce(Machine.parse(it)) }
println "All lights: ${machines*.solution()*.length.sum()}"

int presses = 0
for (m in machines) {
	def coef = new int[m.jolt().length][m.buttons().length]
	m.buttons().eachWithIndex { b, int idx ->
		for (int i = 0; b > 0; i++, b >>= 1) {
			if ((b & 1) == 1 && i < m.jolt().length) {
				coef[i][idx] = 1
			}
		}
	}

	def model = new ExpressionsBasedModel()
	def vars = (0..<coef[0].length).collect {
		model.newVariable("x$it")
				.lower(0)
				.weight(1)
				.integer()
	}

	(0..<coef.length).collect { cIdx ->
		def c = model.newExpression("c$cIdx").level(m.jolt()[cIdx])
		vars.eachWithIndex { v, vIdx ->
			if (coef[cIdx][vIdx]) {
				c.set(v, coef[cIdx][vIdx])
			}
		}
	}

	def res = model.minimise()
	if (res.state.optimal || res.state.feasible) {
		presses += res.toList().sum { it.round() as int }
		continue
	}

	throw new RuntimeException("$m has no solution")
}

println "All jolts: $presses"