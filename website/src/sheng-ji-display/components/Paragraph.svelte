<div class="z-10 relative flex justify-center mt-16 mx-12 pb-[40dvh]">
    <div class="flex flex-col w-[36rem] space-y-2">
        <div class="border-solid border-slate-600 border-2 rounded-full w-fit py-1 px-4 backdrop-blur-lg text-slate-200 text-base font-semibold">
            <slot name="header" />
        </div>
        <div class="font-semibold text-2xl drop-shadow-lg">
            <slot name="body" />
        </div>
    </div>
</div>
