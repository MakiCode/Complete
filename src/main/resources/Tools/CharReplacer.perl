#!/usr/bin/perl

use 5.010;
use strict;
use warnings;

my $file = $ARGV[0];
my $charToReplace = $ARGV[1];
my $replaceWith = $ARGV[2];  
print "opening file...\n";
open(FILE, $file);
my @lines = <FILE>;
close FILE;
open(FILE, ">".$file);
foreach my $string (@lines) {
	my $stringToPrint;
	for (my $key = 0; $key < length($string); $key++) {
		my $char = substr ($string, $key, 1);
		if($char eq $charToReplace) {
			$char = $replaceWith;
		}
		$stringToPrint .= $char;
	}
	print FILE $stringToPrint;
}
print "closing file...\n";
close FILE;