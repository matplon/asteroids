import {pathDataToPolys} from "./svgToPolygon";

let pathData = 'M 173.74863,174.49198 184.79787,170.48665 195.57084,162.47598 204.13397,151.56489 210.07292,136.23413 209.65857,117.5886 203.16717,103.08653 193.63723,92.727904 175.8204,84.441006 158.41791,84.302891 142.39657,91.899214 132.17606,103.63899 127.20392,117.45049 127.75638,132.91937 133.69532,144.52103 144.4683,154.18908 155.24127,158.47064 168.08596,158.60876 181.48311,152.80793 191.15116,141.75873 194.18969,128.36157 189.35567,112.20212 179.5495,103.36276 166.70481,99.909884 152.34085,104.74391 143.22526,116.34557 143.08715,127.53288 148.47363,136.51036 156.76053,142.17307 169.19088,140.93004 177.47778,131.4001 176.51097,122.28451 169.60522,116.48368 163.25194,117.31237 159.2466,122.28451 V 122.28451 L 159.93718,125.46116 161.45644,126.84231 164.63308,125.59927 161.45644,126.84231 159.93718,125.46116 159.2466,122.28451 163.25194,117.31237 169.60522,116.48368 176.51097,122.28451 177.47778,131.4001 169.19088,140.93004 156.76053,142.17307 148.47363,136.51036 143.08715,127.53288 143.22526,116.34557 152.34085,104.74391 166.70481,99.909884 179.5495,103.36276 189.35567,112.20212 194.18969,128.36157 191.15116,141.75873 181.48311,152.80793 168.08596,158.60876 155.24127,158.47064 144.4683,154.18908 133.69532,144.52103 127.75638,132.91937 127.20392,117.45049 132.17606,103.63899 142.39657,91.899214 158.41791,84.302891 175.8204,84.441006 193.63723,92.727904 203.16717,103.08653 209.65857,117.5886 210.07292,136.23413 204.13397,151.56489 195.57084,162.47598 184.79787,170.48665 Z';
let points = pathDataToPolys(pathData);
console.log(points);