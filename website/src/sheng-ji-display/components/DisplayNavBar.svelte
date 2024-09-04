<script lang="ts">
    import shengJi from "../../assets/sheng-ji.svg";

    export let versionNumber: string;
</script>

<div
    class="h-16 w-full z-50 fixed top-0 flex items-center justify-between p-4 backdrop-brightness-90 backdrop-blur-lg border-b-[0.5px] border-b-slate-600"
>
    <div class="flex items-center space-x-4">
        <a href="/" class="h-8 w-8">
            <img src={shengJi.src} alt="升级" class="h-8" />
        </a>
        <div class="h-6 w-[1px] bg-slate-500" />
        <a href="/" class="h-8 w-8">
            <img src="/sheng-ji-display.svg" alt="升级" class="h-8" />
        </a>
        <p class="hidden sm:block font-semibold text-xl">升级 Display</p>
        <p class="font-medium text-sm text-center bg-slate-700 px-2 py-1 rounded-lg text-nowrap">
            v{versionNumber}
        </p>
    </div>
    <div class="flex items-center space-x-6">
        <a href="/display/downloads" class="hidden sm:block text-base font-semibold">Download</a>
        <a
            href="https://l.shengji.edwinchang.dev/display"
            class="text-base font-semibold bg-blue-600 hover:bg-blue-500 px-6 py-3 rounded-full shadow-sm hover:shadow-2xl transition"
        >
            Open
            <p class="hidden sm:inline text-nowrap">升级 Display</p>
        </a>
    </div>
</div>
