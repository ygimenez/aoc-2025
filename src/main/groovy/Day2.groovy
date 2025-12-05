def input = getClass().getResource('/day2.txt').text

println input.split(',')*.split('-')
		.collect { it.collect { it as long } as Range<Long> }
		.flatten { it.findAll { it =~ /^(.+)\1+$/ } }
		.sum()