def input = getClass().getResource('/day2.txt').text

println input.split(',')*.split('-')
		.collect { it*.toLong() as NumberRange }
		.collectMany { it.findAll { it =~ /^(.+)\1+$/ } }
		.sum()