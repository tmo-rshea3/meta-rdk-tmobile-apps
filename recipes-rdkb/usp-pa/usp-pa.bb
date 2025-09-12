#
# Yocto recipe to install obuspa open source project
#

SUMMARY = "USP Pa component"
DESCRIPTION = "Agent for USP protocol"
DEPENDS = "obuspa ccsp-common-library rbus"
RDEPENDS_${PN} += "obuspa"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/usp-pa-vendor-rdk/LICENSE;md5=778849279f710b843cfcef75fe59376b"

require recipes-ccsp/ccsp/ccsp_common.inc

# USPPA is the RDK specializations
#SRC_URI += "git://github.com/rdkcentral/usp-pa-vendor-rdk;protocol=http;branch=main;rev=746b770744f074cf6a2728f3e6e59d2b9cbee4e2;name=usppa;destsuffix=usp-pa-vendor-rdk"
SRC_URI += "git://github.com/tmo-rshea3/usp-pa-vendor-rdk;protocol=http;branch=rshea/OBUSPA-391;name=usppa;destsuffix=usp-pa-vendor-rdk"

SRCREV = "c465ee2e1edc9b123a131a3637668a2e58bbc550"
PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/usp-pa-vendor-rdk/src/vendor"

# Configuration files for target
SRC_URI += "file://conf/usp_factory_reset.conf"
SRC_URI += "file://conf/usp_dm_objs.conf"
SRC_URI += "file://conf/usp_dm_params.conf"
#SRC_URI += "file://conf/usp_truststore.pem"
SRC_URI += "file://usp-pa.service"

# Specify the rules to use to build and install this package
inherit autotools pkgconfig systemd

EXTRA_OECONF = " \
    --with-sysroot=${STAGING_DIR_TARGET} \
    "

# Copy files to staging area
do_install_append() {
    install -d ${D}${sysconfdir}/usp-pa
    install -d ${D}${systemd_system_unitdir}

    install -m 0644 ${WORKDIR}/conf/usp_factory_reset.conf ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/usp_dm_objs.conf ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/usp_dm_params.conf ${D}${sysconfdir}/usp-pa
    #install -m 0644 ${WORKDIR}/conf/usp_truststore.pem ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/usp-pa.service ${D}${systemd_system_unitdir}
}

# Files in staging area to copy to system image
FILES_${PN} += "${sysconfdir}/usp-pa/usp_factory_reset.conf"
FILES_${PN} += "${sysconfdir}/usp-pa/usp_dm_objs.conf"
FILES_${PN} += "${sysconfdir}/usp-pa/usp_dm_params.conf"
#FILES_${PN} += "${sysconfdir}/usp-pa/usp_truststore.pem"

# Signal that a system-d service must be provisioned
SYSTEMD_SERVICE_${PN} = "usp-pa.service"

## Additional steps for DAC Distro Feature
TARGET_CFLAGS  += "${@bb.utils.contains('DISTRO_FEATURES', 'dac', ' -DINCLUDE_LCM_DATAMODEL ', '', d)}"
