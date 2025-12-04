def input = getClass().getResource('/day4.txt').readLines()
boolean COUNT_ONLY = false

def mat = input.flatten { it.split('') as List<String> } as List<String>
int cols = input.first.length()
int rows = input.size()
int total = 0

boolean loop = true
while (loop) {
	loop = false

	for (i in mat.indices) {
		if (mat[i] == '@') {
			def (x, y) = [i % cols, i / cols as int]
			int count = (Math.max(0, y - 1)..Math.min(y + 1, rows - 1)).collect {
				int col = cols * it
				return mat.subList(
						col + Math.max(0, x - 1),
						col + Math.min(x + 2, rows)
				).count { it == '@' }
			}.sum() as int

			if (count <= 4) {
				total++

				if (!COUNT_ONLY) {
					mat[i] = '.'
					loop = true
				}
			}
		}
	}
}

println total