def input = getClass().getResource('/day2.txt').text

println input.split(',')*.split('-')
		.collect { it*.toBigInteger() as NumberRange }
		.flatten { it.findAll { it =~ /^(.+)\1$/ } }
		.sum()