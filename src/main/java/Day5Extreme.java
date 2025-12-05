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
	var list = new ArrayList<long[]>();

	boolean counting = false;
	for (String s : input) {
		if (!counting) {
			if (s.isBlank()) {
				optimizeRanges(list);
				counting = true;
				continue;
			}

			long[] entry = new long[3];
			System.arraycopy(Arrays.stream(s.split("-")).mapToLong(Long::parseLong).toArray(), 0, entry, 0, 2);
			list.add(entry);
			continue;
		}

		long val = Long.parseLong(s);
		for (long[] r : list) {
			if (val >= r[0] && val <= r[1]) {
				r[2] += 1;
				break;
			}
		}
	}

	long[] out = new long[2];
	for (long[] v : list) {
		out[0] += v[2];
		out[1] += v[1] - v[0] + 1;
	}

	return out;
}

void optimizeRanges(List<long[]> ranges) {
	int rangesSize = ranges.size();
	ranges.sort(Comparator.comparingLong(r -> r[0]));

	loop:
	for (int i = 0; i < rangesSize; i++) {
		long[] r = ranges.get(i);
		for (int j = i + 1; j < rangesSize; j++) {
			var other = ranges.get(j);
			if (r[1] < other[0]) continue loop;

			r[1] = Math.max(r[1], other[1]);
			ranges.remove(j--);
			rangesSize--;
		}
	}

	System.out.println(ranges.stream().map(Arrays::toString).toList());
}