def input = getClass().getResource('/day12.txt').readLines()

record Tree(int w, int h, int[] quota) {}

record Present(boolean[][] shape, int area) {}

def trees = [] as List<Tree>
def presents = [] as List<Present>

int rowIdx = 0
int presIdx = -1
int presArea = 0
boolean[][] currPres = null
boolean treeMode = false
input.each {
	if (!treeMode && it ==~ /\d+x\d+:.+/) {
		treeMode = true
		if (currPres != null) {
			presents << new Present(currPres, presArea)
		}
	}

	if (treeMode) {
		def (sz, qt) = it.split(': ').collect { it }
		def (w, h) = sz.split('x').collect { it as int }
		trees << new Tree(w, h, qt.split(' ').collect { it as int } as int[])
	} else {
		if (it ==~ /[0-9]:/) {
			presIdx++
			rowIdx = 0

			if (currPres != null) {
				presents << new Present(currPres, presArea)
				currPres = null
				presArea = 0
			}
		} else if (!it.blank) {
			if (currPres == null) {
				currPres = new boolean[it.length()][]
			}

			currPres[rowIdx++] = it.chars.collect {
				if (it == '#') {
					presArea++
					return true
				}

				return false
			} as boolean[]
		}
	}
}

println trees.count { t ->
	int total = presents.withIndex()
			.collect { Present p, int i -> p.area() * t.quota()[i] }
			.sum() as int
	return total <= t.w() * t.h()
}