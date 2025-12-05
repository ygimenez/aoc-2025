static final long[] clear = new long[3];

void main() throws URISyntaxException, IOException {
	List<String> input = Files.readAllLines(Path.of(getClass().getResource("/day5.txt").toURI()));

	int warm = 0;
	while (warm++ < 1000) {
		System.out.println("Warmup(" + warm + "): " + Arrays.toString(task(input)));
	}

	long time = System.nanoTime();
	long[] res = task(input);
	time = System.nanoTime() - time;

	System.out.println("Result: " + Arrays.toString(res));
	System.out.println("Time: " + (time / 1e6) + "ms");
}

long[] task(List<String> input) {
	long[][] ranges = null;
	int rangesSize = 0;

	boolean counting = false;
	//noinspection ForLoopReplaceableByForEach
	for (int j = 0, inputSize = input.size(); j < inputSize; j++) {
		String s = input.get(j);
		if (!counting) {
			if (s.isBlank()) {
				optimizeRanges(ranges);
				counting = true;
				continue;
			}

			long[] entry = new long[3];
			System.arraycopy(Arrays.stream(s.split("-")).mapToLong(Long::parseLong).toArray(), 0, entry, 0, 2);

			if (rangesSize == 0) {
				ranges = new long[4][];
			} else if (ranges.length == rangesSize) {
				final long[][] aux = new long[rangesSize * 2][];
				System.arraycopy(ranges, 0, aux, 0, ranges.length);
				ranges = aux;
			}

			ranges[rangesSize++] = entry;
			continue;
		}

		long val = Long.parseLong(s);
		for (int i = 0; i < rangesSize; i++) {
			long[] r = ranges[i];
			if (r == null) break;
			if (r == clear) continue;

			if (val >= r[0] && val <= r[1]) {
				r[2] += 1;
				break;
			}
		}
	}

	long[] out = new long[2];
	for (int i = 0; i < rangesSize; i++) {
		long[] r = ranges[i];
		if (r == null) break;
		if (r == clear) continue;

		out[0] += r[2];
		out[1] += r[1] - r[0] + 1;
	}

	return out;
}

void optimizeRanges(long[][] ranges) {
	Arrays.sort(ranges, Comparator.comparingLong(r -> r == null ? Long.MAX_VALUE : r[0]));
	final int rangesSize = ranges.length;

	loop:
	for (int i = 0; i < rangesSize; i++) {
		final long[] r = ranges[i];
		if (r == null) break;
		if (r == clear) continue;

		for (int j = i + 1; j < rangesSize; j++) {
			final long[] other = ranges[j];
			if (other == null || r[1] < other[0]) continue loop;

			r[1] = Math.max(r[1], other[1]);
			ranges[j] = clear;
		}
	}
}