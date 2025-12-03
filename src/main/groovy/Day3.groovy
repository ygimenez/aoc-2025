def input = getClass().getResource('/day3.txt').readLines()

println input
		.collect { it.split('') as List<String> }
		*.with {
			def head = it.dropRight(1).max()
			return head + it.drop(it.indexOf(head) + 1).max() as int
		}
		.sum()