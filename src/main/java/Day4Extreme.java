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

	long time = System.currentTimeMillis();
	int res = task(mat, cols);
	System.out.println("Result: " + res);
	System.out.println("Time: " + (System.currentTimeMillis() - time) + "ms");
}

int task(int[] input, int cols) {
	int[] mat = new int[input.length];
	System.arraycopy(input, 0, mat, 0, input.length);

	int[] changed = null;
	int changedSize, rm = 0;

	final int rows = input.length / cols; // precompute once

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


				int count = fillNearbyAndCount(mat, idx, cols, rows);

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

static int fillNearbyAndCount(int[] input, int idx, int cols, int rows) {
	int count = 0;

	final int x = idx % cols;
	final int y = idx / cols;

	// above row
	if (y > 0) {
		int base = cols * (y - 1);
		int v0 = (x > 0) ? input[base + (x - 1)] : 0;
		int v1 = input[base + x];
		int v2 = (x < cols - 1) ? input[base + (x + 1)] : 0;
		nearby[0] = v0; count += ((v0 & 0xFF) == CHR_CODE) ? 1 : 0;
		nearby[1] = v1; count += ((v1 & 0xFF) == CHR_CODE) ? 1 : 0;
		nearby[2] = v2; count += ((v2 & 0xFF) == CHR_CODE) ? 1 : 0;
	} else {
		nearby[0] = 0;
		nearby[1] = 0;
		nearby[2] = 0;
	}

	// same row
	int baseMid = cols * y;
	int v3 = (x > 0) ? input[baseMid + (x - 1)] : 0;
	int v4 = (x < cols - 1) ? input[baseMid + (x + 1)] : 0;
	nearby[3] = v3; count += ((v3 & 0xFF) == CHR_CODE) ? 1 : 0;
	nearby[4] = v4; count += ((v4 & 0xFF) == CHR_CODE) ? 1 : 0;

	// below row
	if (y < rows - 1) {
		int base = cols * (y + 1);
		int v5 = (x > 0) ? input[base + (x - 1)] : 0;
		int v6 = input[base + x];
		int v7 = (x < cols - 1) ? input[base + (x + 1)] : 0;
		nearby[5] = v5; count += ((v5 & 0xFF) == CHR_CODE) ? 1 : 0;
		nearby[6] = v6; count += ((v6 & 0xFF) == CHR_CODE) ? 1 : 0;
		nearby[7] = v7; count += ((v7 & 0xFF) == CHR_CODE) ? 1 : 0;
	} else {
		nearby[5] = 0;
		nearby[6] = 0;
		nearby[7] = 0;
	}

	return count;
}