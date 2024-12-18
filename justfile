build-web:
    ./gradlew mergeWeb
    cd guide && pnpm i && pnpm next build
    cp -rnv guide/out/* build/web/public
    cd build/web && pnpm i && pnpm astro build
