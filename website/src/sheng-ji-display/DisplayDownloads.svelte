<script lang="ts">
    import android from "../assets/android.svg";
    import windows from "../assets/windows.svg";
    import linux from "../assets/linux.svg";

    export let versionNumber: string;
</script>

<div class="min-h-dvh bg-slate-900 flex justify-center pt-24 pb-8 px-8">
    <div class="flex flex-col space-y-8 w-[48rem]">
        <h1 class="text-4xl font-bold">
            Download
            <p class="inline text-nowrap">升级 Display</p>
        </h1>
        <p class="text-slate-400 font-medium">Version {versionNumber}</p>
        <h2 class="text-2xl font-medium">Android</h2>
        <a
            class="flex space-x-4 items-center rounded-xl border-2 border-slate-300 w-fit px-6 py-3"
            href="https://github.com/EdwinChang24/sheng-ji/releases/download/display%2F{versionNumber}/sheng-ji-display-{versionNumber}.apk"
        >
            <img src={android.src} alt="Android logo" class="h-4" />
            <p>Download for Android</p>
        </a>
        <h2 class="text-2xl font-medium">Desktop</h2>
        <div class="flex flex-col sm:flex-row space-y-8 sm:space-y-0 sm:space-x-8">
            <a
                class="flex space-x-4 items-center rounded-xl border-2 border-slate-300 w-fit px-6 py-3"
                href="https://github.com/EdwinChang24/sheng-ji/releases/download/display%2F{versionNumber}/sheng-ji-display-{versionNumber}.exe"
            >
                <img src={windows.src} alt="Windows logo" class="h-4" />
                <p>Download for Windows</p>
            </a>
            <a
                class="flex space-x-4 items-center rounded-xl border-2 border-slate-300 w-fit px-6 py-3"
                href="https://github.com/EdwinChang24/sheng-ji/releases/download/display%2F{versionNumber}/sheng-ji-display-{versionNumber}.deb"
            >
                <img src={linux.src} alt="Linux logo" class="h-4" />
                <p>Download for Linux (.deb)</p>
            </a>
        </div>
        <div class="self-center pt-16">
            <div
                class="max-w-[48rem] flex flex-col sm:flex-row space-y-4 sm:space-y-0 sm:space-x-8 items-center bg-slate-800 border-2 border-slate-400 px-6 py-4 rounded-xl"
            >
                <p class="text-center">Don't see your platform above?</p>
                <a
                    href="/display"
                    class="px-6 py-3 rounded-full bg-slate-500 hover:bg-slate-400 transition-colors font-semibold text-center"
                >
                    Open in your browser
                </a>
            </div>
        </div>
    </div>
</div>
