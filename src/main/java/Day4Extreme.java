static final int CHR_CODE = '@';
static final int BLANK_CODE = '.';
static final int EXIT_CODE = 0xFF;
static final boolean COUNT_ONLY = false;
static final int[] clear = new int[8];
static int[] nearby = new int[8];

void main() throws IOException, URISyntaxException {
	List<String> input = Files.readAllLines(Path.of(getClass().getResource("/day4.txt").toURI()));
	int[] chars = String.join("", input).chars().toArray();
	int[] mat = IntStream.range(0, chars.length)
			.map(i -> (i << 8) | chars[i])
			.toArray();

	int cols = input.getFirst().length();

	int warm = 0;
	while (warm++ < 1000) {
		System.out.println("Warmup(" + warm + "): " + task(mat, cols));
	}

	long time = System.nanoTime();
	int res = task(mat, cols);
	time = System.nanoTime() - time;

	System.out.println("Result: " + res);
	System.out.println("Time: " + (time / 1e6) + "ms");
}

int task(int[] input, int cols) {
	int[] mat = new int[input.length];
	System.arraycopy(input, 0, mat, 0, input.length);

	int[] changed = null;
	int changedSize, rm = 0;

	boolean loop = true;
	while (loop) {
		loop = false;

		final int[] scan = changed == null ? mat : changed;
		changed = null;
		changedSize = 0;

		final int scanLength = scan.length;
		//noinspection ForLoopReplaceableByForEach
		for (int i = 0; i < scanLength; i++) {
			final int v = scan[i];
			final int idx = v >> 8;
			final int chr = v & 0xFF;

			if (chr == EXIT_CODE) break;
			else if (chr == CHR_CODE) {
				if (scan != mat) {
					if ((mat[idx] & 0xFF) != CHR_CODE) continue;
				}

				getNearby(mat, idx, cols);

				int count = 0;
				for (int j = 0; j < 8 && count < 4; j++) {
					if ((nearby[j] & 0xFF) == CHR_CODE) {
						count++;
					}
				}

				if (count != 4) {
					rm++;

					if (!COUNT_ONLY) {
						mat[idx] = BLANK_CODE;
						loop = true;

						if (changedSize == 0) {
							changed = new int[9];
						} else if (changed.length == changedSize + 1) {
							final int[] aux = new int[changedSize * 2 + 1];
							System.arraycopy(changed, 0, aux, 0, changed.length);
							changed = aux;
						}

						System.arraycopy(nearby, 0, changed, changedSize, nearby.length);
						changed[changedSize += 8] = EXIT_CODE;
					}
				}
			}
		}
	}

	return rm;
}

static void getNearby(int[] input, int idx, int cols) {
	System.arraycopy(clear, 0, nearby, 0, clear.length);

	final int x = idx % cols;
	final int y = idx / cols;
	final int rows = input.length / cols;

	if (y > 0) {
		nearby[0] = x > 0 ? input[cols * (y - 1) + (x - 1)] : 0;
		nearby[1] = input[cols * (y - 1) + x];
		nearby[2] = x < cols - 1 ? input[cols * (y - 1) + (x + 1)] : 0;
	}

	nearby[3] = (x > 0) ? input[cols * y + (x - 1)] : 0;
	nearby[4] = (x < cols - 1) ? input[cols * y + (x + 1)] : 0;

	if (y < rows - 1) {
		nearby[5] = (x > 0) ? input[cols * (y + 1) + (x - 1)] : 0;
		nearby[6] = input[cols * (y + 1) + x];
		nearby[7] = (x < cols - 1) ? input[cols * (y + 1) + (x + 1)] : 0;
	}
}