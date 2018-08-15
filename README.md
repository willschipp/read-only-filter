# Read Only Filter

A simple Spring Boot Filter that enables blocking of anything but GET requests

## Background

When performing a database update in a cloud context and trying to do a blue/green deployment, commonly database synchronizing can be undermined by data changes.  In a scneario where trying to sync a new database with an old whilst still online, this filter enables you to keep the existing service online, but switch to a Read-Only format.  It does so by blocking anything but GET requests.