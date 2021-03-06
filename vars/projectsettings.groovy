@groovy.transform.Field
def buildNumber = "-1"

@groovy.transform.Field
def version = "0.0.0"

@NonCPS
def getBundleVersion(root) {
	filename = root + '/ProjectSettings/ProjectSettings.asset'

	// read all the lines into a list, each line is an element in the list
	File fh = new File(filename)
	def lines = fh.readLines()
	lines.eachWithIndex{ it, i -> 
		def tag2 = "bundleVersion: "
		if(it.indexOf(tag2) != -1) {
			def split2 = it.split(tag2)
			version = split2[1]
			return version
		}
	}
	version = "0.0.0"
	return "0.0.0"
}

@NonCPS
def getAndroidBuildNumber(root) {
	filename = root + '/ProjectSettings/ProjectSettings.asset'

	// read all the lines into a list, each line is an element in the list
	File fh = new File(filename)
	def lines = fh.readLines()
	lines.eachWithIndex{ it, i -> 
		def tag = "AndroidBundleVersionCode: "
		if(it.indexOf(tag) != -1) {
			def split1 = it.split(tag)
			buildNumber = split1[1]
			return buildNumber
		}
	}
	buildNumber = "-2"
	return "-2"
}


@NonCPS
def increaseAndroidBuildNumber(root) {
	filename = root + '/ProjectSettings/ProjectSettings.asset'

	// read all the lines into a list, each line is an element in the list
	File fh = new File(filename)
	def lines = fh.readLines()
	lines.eachWithIndex{ it, i -> 
		def tag = "AndroidBundleVersionCode: "
		if(it.indexOf(tag) != -1) {
			def split1 = it.split(tag)
			def newVersion = Integer.parseInt(split1[1]) + 1
			buildNumber = newVersion.toString()
			lines[i] = split1[0] + tag + newVersion
		}
	}

	fh.withWriter('utf8') {
		writer -> lines.each { line -> 
			writer.writeLine(line)
		}
	}
}

@NonCPS
def increaseBundlePatchVersion(root) {
	filename = root + '/ProjectSettings/ProjectSettings.asset'

	// read all the lines into a list, each line is an element in the list
	File fh = new File(filename)
	def lines = fh.readLines()
	lines.eachWithIndex{ it, i -> 
		def tag2 = "bundleVersion: "
		//
		// Find the bundleVersion line
		//
		if(it.indexOf(tag2) != -1) {
			//
			// split2 contains the version bit of the bundleVersion line as string
			// e.g.: ["  ", "3.0.6"]
			// the first array entry is 2 spaces because of the way ProjectSettings is indented
			//
			def split2 = it.split(tag2)
			//
			// versionSplit contains the major, minor and patch version as string
			// e.g.: ["3","0","6"]
			//
			def versionSplit = split2[1].split("\\.")
			if(versionSplit.size() >= 3) {
				def patchVersion = Integer.parseInt(versionSplit[2]) + 1
				versionSplit[2] = patchVersion
			}
			def newVersion = ""
			for(i2 in 0 .. versionSplit.size()-1) {
				newVersion += versionSplit[i2]
				if(i2 < versionSplit.size() - 1) {
					newVersion += "."
				}
			}
			//
			// store new version globally for easy access from pipeline
			//
			version = newVersion
			lines[i] = split2[0] + tag2 + newVersion
		}
	}

	fh.withWriter('utf8') {
		writer -> lines.each { line -> 
			writer.writeLine(line)
		}
	}
}