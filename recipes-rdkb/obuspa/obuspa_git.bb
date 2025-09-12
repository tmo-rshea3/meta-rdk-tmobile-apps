SUMMARY = "OB-USP-AGENT"
DESCRIPTION = "Open Broadband-User Services Platform-Agent (OB-USP-Agent) is an open source project that is focused on creating a reference implementation of the User Services Platform (USP) specification from an "Agent" perspective"
DEPENDS = "curl openssl sqlite3 zlib"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1fcc8a31dcd8bb90cb70510dd3fd17d2"

#SRC_URI += "git://github.com/BroadbandForum/obuspa;protocol=http;branch=master;name=obuspa"
SRC_URI += "git://github.com/tmo-rshea3/obuspa;protocol=http;branch=rshea/OBUSPA-391;name=obuspa"

SRCREV = "4904d320d0e498a3ef2a6b19cb96d1df566f3570"
PV = "10.0.8+git${SRCPV}"

S = "${WORKDIR}/git"

# Configure options for OBUSPA
PACKAGECONFIG ??= "mqtt uds"
PACKAGECONFIG[bulkdata] = "--enable-bulkdata,--disable-bulkdata"
PACKAGECONFIG[coap] = "--enable-coap,--disable-coap"
PACKAGECONFIG[hardening] = "--enable-hardening,--disable-hardening"
PACKAGECONFIG[mqtt] = "--enable-mqtt,--disable-mqtt"
PACKAGECONFIG[stomp] = "--enable-stomp,--disable-stomp"
PACKAGECONFIG[uds] = "--enable-uds,--disable-uds"
PACKAGECONFIG[websockets] = "--enable-websockets,--disable-websockets"

# Build dependencies based on configure options
DEPENDS += "${@bb.utils.contains('PACKAGECONFIG', 'mqtt', 'mosquitto', '', d)}"
DEPENDS += "${@bb.utils.contains('PACKAGECONFIG', 'websockets', 'libwebsockets', '', d)}"

inherit autotools pkgconfig
