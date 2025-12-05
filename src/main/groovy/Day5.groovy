def input = getClass().getResource('/day5.txt').readLines()

def map = new TreeMap<Tuple2<Long, Long>, Integer>((a, b) -> a.v1 <=> b.v1 ?: a.v2 <=> b.v2)
def ranges = input.inject(map) {
	tot, v ->
		if (!v.empty) {
			def (a, b) = v.split('-').collect { it as long }

			if (b != null) {
				tot[Tuple.tuple(a, b)] = 0
			} else {
				for (p in tot.keySet()) {
					if (a >= p.v1 && a <= p.v2) {
						tot[p] += 1
						break
					}
				}
			}
		}

		return tot
}

def ids = ranges.keySet().toList()
def merged = new ArrayList<Tuple2<Long, Long>>()
for (id in ids) {
	def (aMin, aMax) = id

	if (!merged || merged.last.v2 < aMin) {
		merged << id
		continue
	}

	def (bMin, bMax) = merged.last
	merged[-1] = Tuple.tuple(bMin, Math.max(aMax, bMax))
}

println "Fresh: ${ranges.values().sum()}"
println "IDs: ${merged.sum { it.v2 - it.v1 + 1 }}"