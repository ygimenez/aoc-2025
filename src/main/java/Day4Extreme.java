int CHR_CODE = '@';
int BLANK_CODE = '.';
boolean COUNT_ONLY = false;

void main() throws IOException, URISyntaxException {
	int warm = 0;
	while (warm++ < 100) {
		System.out.println("Warmup(" + warm + "): " + task());
	}

	long time = System.currentTimeMillis();
	int res = task();
	System.out.println("Result: " + res);
	System.out.println("Time: " + (System.currentTimeMillis() - time) + "ms");
}

int task() throws URISyntaxException, IOException {
	List<String> input = Files.readAllLines(Path.of(getClass().getResource("/day4.txt").toURI()));
	int[] chars = String.join("", input).chars().toArray();
	int[] mat = IntStream.range(0, chars.length)
			.map(i -> (i << 8) | chars[i])
			.toArray();

	int[] nearby = new int[8], changed = new int[0];
	int changedSize = 0, rm = 0, cols = input.getFirst().length();

	boolean loop = true;
	while (loop) {
		loop = false;

		final int[] scan = changedSize == 0 ? mat : changed;
		changed = new int[0];
		changedSize = 0;

		for (int i : scan) {
			final int idx = i >> 8;
			final int chr = i & 0xFF;

			if (chr == CHR_CODE) {
				if (scan != mat) {
					if ((mat[idx] & 0xFF) != CHR_CODE) continue;
				}

				getNearby(mat, idx, cols, nearby);

				int count = 0;
				for (int j = 0; j < nearby.length && count < 4; j++) {
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
							changed = new int[8];
						} else if (changed.length == changedSize) {
							final int[] aux = new int[changedSize * 2];
							System.arraycopy(changed, 0, aux, 0, changed.length);
							changed = aux;
						}

						System.arraycopy(nearby, 0, changed, changedSize, nearby.length);
						changedSize += 8;
					}
				}
			}
		}
	}

	return rm;
}

static void getNearby(int[] input, int idx, int cols, int[] out) {
	final int x = idx % cols;
	final int y = idx / cols;
	final int rows = input.length / cols;

	out[0] = (y > 0 && x > 0) ? input[cols * (y - 1) + (x - 1)] : 0;
	out[1] = (y > 0) ? input[cols * (y - 1) + x] : 0;
	out[2] = (y > 0 && x < cols - 1) ? input[cols * (y - 1) + (x + 1)] : 0;

	out[3] = (x > 0) ? input[cols * y + (x - 1)] : 0;
	out[4] = (x < cols - 1) ? input[cols * y + (x + 1)] : 0;

	out[5] = (y < rows - 1 && x > 0) ? input[cols * (y + 1) + (x - 1)] : 0;
	out[6] = (y < rows - 1) ? input[cols * (y + 1) + x] : 0;
	out[7] = (y < rows - 1 && x < cols - 1) ? input[cols * (y + 1) + (x + 1)] : 0;
}