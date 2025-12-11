record Coord(long x, long y, long z) {
	double dist(Coord other) {
		return Math.sqrt((other.x - x)**2 + (other.y - y)**2 + (other.z - z)**2 as double)
	}
}

def input = getClass().getResource('/day8.txt').readLines()
		.collect { it.split(',')*.toInteger() as Coord }

def pairs = [] as List<Tuple2<List<Coord>, Double>>
for (i in 0..<(input.size() - 1)) {
	def a = input[i]
	for (j in (i + 1)..<input.size()) {
		def b = input[j]
		pairs << Tuple.tuple([a, b], a.dist(b))
	}
}
pairs.sort { it.v2 }

int max = 1000
def groups = [] as List<Set<Coord>>
for (pair in pairs*.v1) {
	if (max-- == 0) {
		def sorted = groups
				.toSorted { -it.size() }
				.take(3)

		println "Top 3: ${sorted*.size().inject { a, b -> a * b }}"
	}

	def (a, b) = pair
	def gps = groups.findAll { a in it || b in it }
	if (gps) {
		def target = gps.first
		gps.drop(1).each {
			target.addAll(it)
			groups.remove(it)
		}

		target << a
		target << b
	} else {
		groups << (pair as Set<Coord>)
	}

	if (groups.size() == 1 && groups.first.size() == input.size()) {
		println "Last link: ${a.x() * b.x()}"
		break
	}
}