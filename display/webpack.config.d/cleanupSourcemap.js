// Replace paths unavailable during compilation with `null`, so they will not be shown in devtools

(() => {
    const fs = require("fs");
    const path = require("path");

    const outDir = __dirname + "/kotlin/";
    const projectName = path.basename(__dirname);
    const mapFileLegacy = outDir + projectName + ".map";
    const mapFile = outDir + projectName + ".wasm.map";

    let sourcemap;
    try {
        sourcemap = JSON.parse(fs.readFileSync(mapFileLegacy));
    } catch (e) {
        try {
            sourcemap = JSON.parse(fs.readFileSync(mapFile));
        } catch (e1) {
            return;
        }
    }
    const sources = sourcemap["sources"];
    srcLoop: for (let i in sources) {
        const srcFilePath = sources[i];
        if (srcFilePath == null) continue;

        const srcFileCandidates = [
            outDir + srcFilePath,
            outDir + srcFilePath.substring("../".length),
            outDir + "../" + srcFilePath,
        ];

        for (let srcFile of srcFileCandidates) {
            if (fs.existsSync(srcFile)) continue srcLoop;
        }

        sources[i] = null;
    }

    fs.writeFileSync(mapFile, JSON.stringify(sourcemap));
})();
