<script lang="ts">
    import Buttons from "../components/Buttons.svelte";

    let headlineText: HTMLDivElement;
    let iconOpacity = 0;
</script>

<div class="min-h-dvh flex flex-col justify-evenly px-16 py-16 bg-slate-950">
    <div class="flex flex-col items-center space-y-12 my-8">
        <div
            class="w-fit rounded-full border-solid border-slate-800 border-2 py-1 px-4 backdrop-blur-lg text-slate-200 text-lg font-semibold text-center text-nowrap"
        >
            升级 Display
        </div>
        <div class="h-48 sm:h-64">
            <img
                class="w-48 sm:w-64 drop-shadow-[0_0_4rem_rgb(255_0_0/0.25)] transition-opacity"
                src="/sheng-ji-display.svg"
                alt="升级 Display"
                style:opacity={iconOpacity}
                on:load={() => {
                    iconOpacity = 1;
                }}
            />
        </div>
    </div>
    <div class="flex flex-col justify-evenly space-y-12 items-center mb-0">
        <div bind:this={headlineText} class="animate-[headline-enter_0.8s_ease-out_1]">
            <div
                class="font-bold text-5xl sm:text-6xl md:text-7xl lg:text-8xl text-center tracking-tight drop-shadow-lg"
            >
                <p class="inline">Level up your</p>
                <p
                    class="inline text-nowrap bg-clip-text text-transparent bg-gradient-to-tr from-red-700 to-white mt-3 -mb-1"
                >
                    升级
                </p>
                <p class="inline">gameplay</p>
            </div>
        </div>
        <Buttons />
    </div>
</div>

<style>
    @keyframes -global-headline-enter {
        from {
            transform: translateY(100px);
            opacity: 0;
        }
        to {
            transform: translateY(0);
            opacity: 100%;
        }
    }
</style>
