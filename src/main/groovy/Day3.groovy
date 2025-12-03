def input = getClass().getResource('/day3.txt').readLines()

int digits = 12
println input
		.collect { it.split('') as List<String> }
		*.with {
			def prio = it.toUnique().toSorted().reversed()
			def queue = it

			return (0..<digits)*.with {i ->
				for (p in prio) {
					int v = queue.dropRight(digits - i - 1).indexOf(p)
					if (v > -1) {
						queue = queue.drop(v + 1)
						return p
					}
				}

				throw 'Reached end of input' as Throwable
			}.join() as long
		}
		.sum()