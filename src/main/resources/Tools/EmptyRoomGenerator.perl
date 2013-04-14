#!/usr/bin/perl

use 5.010;
use strict;
use warnings;

my $file = $ARGV[0];
my $lengthOfLine = $ARGV[1];
print "opening output file...\n";
unless(open OUTPUT, '>'.$file) {
	die "\nUnable to create '$file'\n";
}
for(my $j = 0; $j < $lengthOfLine; $j++) {
	my $myString = "";
	for(my $i = 0; $i < $lengthOfLine; $i++) {
		if($i == $lengthOfLine - 1) {
			$myString .= ".\n";
		} else {
			$myString .= ". ";
		}
	}
	print OUTPUT $myString;
}
print "closing output file...\n";
close OUTPUT;
